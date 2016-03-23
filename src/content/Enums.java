package content;


//Enum holding snake's life status
enum LifeStatus {
    ALIVE(1),
    DEAD(0),
    RESIGNED(-1);
    public final int value;

    /*  enum constructor   */
    LifeStatus(int value) {
        this.value = value;
    }
}

//Enum holding types of barriers
enum BarrierType {
    EMPTY(0),
    BLUE_SNAKE(1),
    WALL(9);
    public final int value;

    /*  initializing Enum Types by value in brackets            */
    BarrierType(int value) {
        this.value = value;
    }
}