package org.mcmmo.mcmmoworldguard.util;

import java.util.LinkedList;

import org.bukkit.Location;

import org.mcmmo.mcmmoworldguard.config.Config;
import org.mcmmo.mcmmoworldguard.mcMMOWorldGuard;

import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class RegionUtils {

    public static boolean canDuelHereWG(Location location) {
        return mcMMOWorldGuard.p.isWorldGuardEnabled() && canDuelHere(getRegion(location));
    }

    private static boolean canDuelHere(String region) {
        boolean isWhitelist = Config.getInstance().getWGUseAsWhitelist();

        if (isListedRegion(region)) {
            return isWhitelist;
        }

        return !isWhitelist;
    }

    private static boolean isListedRegion(String region) {
        for (String name : Config.getInstance().getWGRegionList()) {
            if (region.equalsIgnoreCase("[" + name + "]")) {
                return true;
            }
        }
        return false;
    }

    public static String getRegion(Location location) {
        RegionManager regionManager = mcMMOWorldGuard.p.getWorldGuard().getRegionManager(location.getWorld());
        ApplicableRegionSet set = regionManager.getApplicableRegions(location);
        LinkedList<String> parentNames = new LinkedList<String>();
        LinkedList<String> regions = new LinkedList<String>();

        for (ProtectedRegion region : set) {
            String id = region.getId();
            regions.add(id);
            ProtectedRegion parent = region.getParent();
            while (parent != null) {
                parentNames.add(parent.getId());
                parent = parent.getParent();
            }
        }

        for (String name : parentNames) {
            regions.remove(name);
        }

        return regions.toString();
    }
}
