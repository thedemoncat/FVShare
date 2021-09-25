package com.google.android.exoplayer.upstream;

import com.google.android.exoplayer.util.Assertions;
import java.util.Arrays;

public final class DefaultAllocator implements Allocator {
    private static final int AVAILABLE_EXTRA_CAPACITY = 100;
    private int allocatedCount;
    private Allocation[] availableAllocations;
    private int availableCount;
    private final int individualAllocationSize;
    private final byte[] initialAllocationBlock;

    public DefaultAllocator(int individualAllocationSize2) {
        this(individualAllocationSize2, 0);
    }

    public DefaultAllocator(int individualAllocationSize2, int initialAllocationCount) {
        boolean z;
        boolean z2 = true;
        if (individualAllocationSize2 > 0) {
            z = true;
        } else {
            z = false;
        }
        Assertions.checkArgument(z);
        Assertions.checkArgument(initialAllocationCount < 0 ? false : z2);
        this.individualAllocationSize = individualAllocationSize2;
        this.availableCount = initialAllocationCount;
        this.availableAllocations = new Allocation[(initialAllocationCount + 100)];
        if (initialAllocationCount > 0) {
            this.initialAllocationBlock = new byte[(initialAllocationCount * individualAllocationSize2)];
            for (int i = 0; i < initialAllocationCount; i++) {
                this.availableAllocations[i] = new Allocation(this.initialAllocationBlock, i * individualAllocationSize2);
            }
            return;
        }
        this.initialAllocationBlock = null;
    }

    public synchronized Allocation allocate() {
        Allocation allocation;
        this.allocatedCount++;
        if (this.availableCount > 0) {
            Allocation[] allocationArr = this.availableAllocations;
            int i = this.availableCount - 1;
            this.availableCount = i;
            allocation = allocationArr[i];
            this.availableAllocations[this.availableCount] = null;
        } else {
            allocation = new Allocation(new byte[this.individualAllocationSize], 0);
        }
        return allocation;
    }

    public synchronized void release(Allocation allocation) {
        Assertions.checkArgument(allocation.data == this.initialAllocationBlock || allocation.data.length == this.individualAllocationSize);
        this.allocatedCount--;
        if (this.availableCount == this.availableAllocations.length) {
            this.availableAllocations = (Allocation[]) Arrays.copyOf(this.availableAllocations, this.availableAllocations.length * 2);
        }
        Allocation[] allocationArr = this.availableAllocations;
        int i = this.availableCount;
        this.availableCount = i + 1;
        allocationArr[i] = allocation;
        notifyAll();
    }

    public synchronized void release(Allocation[] allocations) {
        boolean z;
        if (this.availableCount + allocations.length >= this.availableAllocations.length) {
            this.availableAllocations = (Allocation[]) Arrays.copyOf(this.availableAllocations, Math.max(this.availableAllocations.length * 2, this.availableCount + allocations.length));
        }
        for (Allocation allocation : allocations) {
            if (allocation.data == this.initialAllocationBlock || allocation.data.length == this.individualAllocationSize) {
                z = true;
            } else {
                z = false;
            }
            Assertions.checkArgument(z);
            Allocation[] allocationArr = this.availableAllocations;
            int i = this.availableCount;
            this.availableCount = i + 1;
            allocationArr[i] = allocation;
        }
        this.allocatedCount -= allocations.length;
        notifyAll();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0057, code lost:
        if (r7 < r11.availableCount) goto L_0x0059;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void trim(int r12) {
        /*
            r11 = this;
            monitor-enter(r11)
            int r8 = r11.individualAllocationSize     // Catch:{ all -> 0x004e }
            int r6 = com.google.android.exoplayer.util.Util.ceilDivide((int) r12, (int) r8)     // Catch:{ all -> 0x004e }
            r8 = 0
            int r9 = r11.allocatedCount     // Catch:{ all -> 0x004e }
            int r9 = r6 - r9
            int r7 = java.lang.Math.max(r8, r9)     // Catch:{ all -> 0x004e }
            int r8 = r11.availableCount     // Catch:{ all -> 0x004e }
            if (r7 < r8) goto L_0x0016
        L_0x0014:
            monitor-exit(r11)
            return
        L_0x0016:
            byte[] r8 = r11.initialAllocationBlock     // Catch:{ all -> 0x004e }
            if (r8 == 0) goto L_0x0059
            r4 = 0
            int r8 = r11.availableCount     // Catch:{ all -> 0x004e }
            int r1 = r8 + -1
            r2 = r1
            r5 = r4
        L_0x0021:
            if (r5 > r2) goto L_0x0051
            com.google.android.exoplayer.upstream.Allocation[] r8 = r11.availableAllocations     // Catch:{ all -> 0x004e }
            r3 = r8[r5]     // Catch:{ all -> 0x004e }
            byte[] r8 = r3.data     // Catch:{ all -> 0x004e }
            byte[] r9 = r11.initialAllocationBlock     // Catch:{ all -> 0x004e }
            if (r8 != r9) goto L_0x0033
            int r4 = r5 + 1
            r1 = r2
        L_0x0030:
            r2 = r1
            r5 = r4
            goto L_0x0021
        L_0x0033:
            com.google.android.exoplayer.upstream.Allocation[] r8 = r11.availableAllocations     // Catch:{ all -> 0x004e }
            r0 = r8[r2]     // Catch:{ all -> 0x004e }
            byte[] r8 = r0.data     // Catch:{ all -> 0x004e }
            byte[] r9 = r11.initialAllocationBlock     // Catch:{ all -> 0x004e }
            if (r8 == r9) goto L_0x0041
            int r1 = r2 + -1
            r4 = r5
            goto L_0x0030
        L_0x0041:
            com.google.android.exoplayer.upstream.Allocation[] r8 = r11.availableAllocations     // Catch:{ all -> 0x004e }
            int r4 = r5 + 1
            r8[r5] = r0     // Catch:{ all -> 0x004e }
            com.google.android.exoplayer.upstream.Allocation[] r8 = r11.availableAllocations     // Catch:{ all -> 0x004e }
            int r1 = r2 + -1
            r8[r2] = r3     // Catch:{ all -> 0x004e }
            goto L_0x0030
        L_0x004e:
            r8 = move-exception
            monitor-exit(r11)
            throw r8
        L_0x0051:
            int r7 = java.lang.Math.max(r7, r5)     // Catch:{ all -> 0x004e }
            int r8 = r11.availableCount     // Catch:{ all -> 0x004e }
            if (r7 >= r8) goto L_0x0014
        L_0x0059:
            com.google.android.exoplayer.upstream.Allocation[] r8 = r11.availableAllocations     // Catch:{ all -> 0x004e }
            int r9 = r11.availableCount     // Catch:{ all -> 0x004e }
            r10 = 0
            java.util.Arrays.fill(r8, r7, r9, r10)     // Catch:{ all -> 0x004e }
            r11.availableCount = r7     // Catch:{ all -> 0x004e }
            goto L_0x0014
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.exoplayer.upstream.DefaultAllocator.trim(int):void");
    }

    public synchronized int getTotalBytesAllocated() {
        return this.allocatedCount * this.individualAllocationSize;
    }

    public synchronized void blockWhileTotalBytesAllocatedExceeds(int limit) throws InterruptedException {
        while (getTotalBytesAllocated() > limit) {
            wait();
        }
    }

    public int getIndividualAllocationLength() {
        return this.individualAllocationSize;
    }
}
