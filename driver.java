class Driver{
    public static void main(String[] args){
        Store store1 = new Store();
        StoreService service1 = new StoreService(store1);

        service1.put(5, 27, (long)100000);
        System.out.println("value = " + service1.get(5));

        service1.put(5, 28, (long)5000);
        System.out.println("value = " + service1.get(5));

        try{
            Thread.sleep(6000);
        }
        catch(Exception e){
            System.out.println("sleep interrupted");
        }

        System.out.println(service1.get(5));

    }

}