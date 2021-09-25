package com.bumptech.glide;

public enum MemoryCategory {
    LOW(0.5f),
    NORMAL(1.0f),
    HIGH(1.5f);
    
    private float multiplier;

    private MemoryCategory(float multiplier2) {
        this.multiplier = multiplier2;
    }

    public float getMultiplier() {
        return this.multiplier;
    }
}
