class ValuePair {
    private int value;
    private long expTime;

    public ValuePair(int val, long et){
        this.value = val;
        this.expTime = et;
    }

    public int getValue(){
        return value;
    }

    public long getExpTime(){
        return expTime;
    }

}