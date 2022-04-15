package Game.Fishes;

import Game.PlaygroundPane;

public abstract class FishWithContext extends GenericFish implements FishState{
    protected FishContext context;

    public FishWithContext() {
    }

    public FishWithContext(PlaygroundPane parentPane, FishContext context) {
        super(parentPane);
        setContext(context);
    }

    public FishWithContext(PlaygroundPane parentPane, FishContext context, double x, double y, double speed){
        super(parentPane, x, y, speed);
        setContext(context);
    }


    @Override
    public void setContext(FishContext context) {
        this.context = context;
    }

    @Override
    public abstract void setImg();

    @Override
    public abstract void setSize();
}
