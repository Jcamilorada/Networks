from BeautifulSoup import BeautifulSoup
import urllib2
import re
from subprocess import call
from os import walk, path
from joblib import Parallel, delayed

hdr = {'User-Agent': 'Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.64 Safari/537.11',
       'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
       'Accept-Charset': 'ISO-8859-1,utf-8;q=0.7,*;q=0.3',
       'Accept-Encoding': 'none',
       'Accept-Language': 'en-US,en;q=0.8',
       'Connection': 'keep-alive'}


def download_file(download_url):
    req = urllib2.Request(download_url, headers=hdr)
    try:
        page = urllib2.urlopen(req)
    except urllib2.HTTPError, e:
        print e.fp.read()
    index = download_url.rfind('/')
    file_name = download_url[index + 1:]
    f = open(file_name, 'w')
    f.write(page.read())
    f.close()


def download_pass_file(match_url):
    req = urllib2.Request(match_url, headers=hdr)
    try:
        page = urllib2.urlopen(req)
    except urllib2.HTTPError, e:
        print e.fp.read()

    html_page = page.read()
    soup = BeautifulSoup(html_page)
    for link in soup.findAll('a', attrs={'href': re.compile("passingdistribution\.pdf$")}):
        download_file(link.get('href'))


def get_fifa_pass_distributions(championship_url, base_url):
    req = urllib2.Request(championship_url, headers=hdr)
    try:
        page = urllib2.urlopen(req)
    except urllib2.HTTPError, e:
        print e.fp.read()

    html_page = page.read()
    soup = BeautifulSoup(html_page)
    matches = []
    for link in soup.findAll('a', attrs={'href': re.compile("^/worldcup/matches/round=")}):
        download_pass_file(base_url + link.get('href'))


def export_fifa_pass_dist_to_csv(file_path, output_path):
    file_name = file_path[file_path.rfind('/') + 1:file_path.rfind('.pdf')]
    file_name_header = 'header_' + file_name + '.csv'
    file_name_body = 'body_' + file_name + '.csv'
    call(['jruby -S tabula '+ file_path + ' -a 20.00,12.75,200.5,561 ' + '-o ' + output_path+file_name_header], shell=True)
    call(['jruby -S tabula ' + file_path + ' -a 200.00,12.75,700.5,561 ' + '-o ' + output_path+file_name_body], shell=True)


def export_fifa_dir(files_path, output_path):
    file_names = []
    for root, dirs, files in walk(files_path):
        for name in files:
            if 'pdf' in name:
                file_names.append(path.join(root, name))
            else:
                continue
    n_jobs = 10
    Parallel(n_jobs=n_jobs, verbose=50)(delayed(export_fifa_pass_dist_to_csv)(file_name, output_path) for file_name in file_names)


export_fifa_dir(
    '/Users/developer3/git/Networks/fifa_2014_pass_distributions/pdf_raw_data/',
    '/Users/developer3/git/Networks/fifa_2014_pass_distributions/csv/'
)

#get_fifa_pass_distributions('http://www.fifa.com/worldcup/archive/brazil2014/matches/index.html', 'http://www.fifa.com/')
