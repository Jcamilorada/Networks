package co.edu.univalle.networks.network;

import co.edu.univalle.networks.network.Network;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class MatchNetworks
{
    private Network networkTeamA;
    private Network networkTeamB;
}
