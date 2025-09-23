package com.hoenn.pokemonleague.enums;

public enum TrainerRegion {
    HOENN("HN", "Hoenn"),
    KANTO("KA", "Kanto"),
    JOHTO("JO", "Johto"),
    SINNOH("SI", "Sinnoh"),
    UNOVA("UN", "Unova"),
    KALOS("KL", "Kalos"),
    ALOLA("AL", "Alola"),
    GALAR("GA", "Galar"),
    PALDEA("PA", "Paldea");

    private final String regionCode;
    private final String regionName;
    TrainerRegion(String regionCode, String regionName) {
        this.regionCode = regionCode;
        this.regionName = regionName;
    }

    public String getRegionCode(){
        return regionCode;
    }

    public String getRegionName(){
        return regionName;
    }

    public static boolean isValidRegionCode(String regionCode){
        try {
            getByRegionCode(regionCode);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public static TrainerRegion getByRegionCode(String regionCode){
        if (regionCode == null || regionCode.trim().isEmpty()){
            throw new IllegalArgumentException("Region code cannot be null or empty");
        }

        String upperRegionCode = regionCode.trim().toUpperCase();
        for (TrainerRegion region : values()){
            if (region.getRegionCode().equals(upperRegionCode)){
                return region;
            }
        }
        throw new IllegalArgumentException("Invalid region code: " + regionCode);
    }
}
