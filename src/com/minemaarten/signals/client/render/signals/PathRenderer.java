package com.minemaarten.signals.client.render.signals;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.minemaarten.signals.rail.network.NetworkRail;
import com.minemaarten.signals.rail.network.RailObjectHolder;
import com.minemaarten.signals.rail.network.mc.MCPos;
import com.minemaarten.signals.rail.network.mc.MCTrain;
import com.minemaarten.signals.rail.network.mc.RailNetworkManager;

public class PathRenderer extends AbstractRailRenderer<MCTrain>{

    @Override
    protected boolean canRender(MCTrain section){
        return section.getCurRoute() != null && !section.getCurRoute().routeRails.isEmpty() && getRootNode(section) != null;
    }

    @Override
    protected boolean isAdjacent(MCTrain s1, MCTrain s2){
        return s1.getCurRoute().routeRails.stream().anyMatch(r -> s2.getCurRoute().routeRails.contains(r));
    }

    @Override
    protected Iterable<MCTrain> getRenderableSections(){
        Stream<MCTrain> allTrains = RailNetworkManager.getClientInstance().getAllTrains();
        return allTrains.filter(this::canRender).collect(Collectors.toList());
    }

    @Override
    protected NetworkRail<MCPos> getRootNode(MCTrain section){
        return (NetworkRail<MCPos>)RailNetworkManager.getClientInstance().getNetwork().railObjects.get(section.getCurRoute().routeRails.get(0));
    }

    @Override
    protected RailObjectHolder<MCPos> getNeighborProvider(MCTrain section){
        return RailNetworkManager.getClientInstance().getNetwork().railObjects.subSelectionForPos(section.getCurRoute().routeRails);
    }

    @Override
    protected boolean shouldTraverse(MCTrain section, NetworkRail<MCPos> rail){
        return true;
    }

}
