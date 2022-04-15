package com.carrot.system.java.com.carrot.core;

import com.carrot.core.impl.Loader;
import com.carrot.core.impl.Starter;

import java.util.ArrayList;
import java.util.List;

public class Bootstrap implements Runnable{
    private Class<?> mainClass;
    private StartFlow starter;
    private LoadFlow loader;

    public Bootstrap (Class<?> mainClass, List<Flow> flows){
        this.mainClass = mainClass;

        if(flows == null || flows.size() == 0){
            flows = new ArrayList<>(2);
            flows.add(new Starter());
            flows.add(new Loader());
        }

        for (Flow flow : flows) {
            if (flow instanceof StartFlow) {
                this.starter = (StartFlow) flow;
            }
            if (flow instanceof LoadFlow) {
                this.loader = (LoadFlow) flow;
            }
        }
    }


    @Override
    public void run() {
        loader.addMain(mainClass);
        loader.execute();
        starter.execute();
    }
}
