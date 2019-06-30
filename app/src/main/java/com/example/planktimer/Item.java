package com.example.planktimer;

public class Item {
        String index;
        String timestamp;
        String plankrecord;

        public String getIndex() {
            return index;
        }
        public String getTimestamp() {
            return timestamp;
        }
    public String getPlankrecord() {
        return plankrecord;
    }
        public Item(String index, String timestamp, String plankrecord) {
            this.index = index;
            this.timestamp = timestamp;
            this.plankrecord = plankrecord;
        }
}
