package cn.easii.example;

import cn.easii.example.handler.PermissionDataProvideHandler;
import cn.easii.example.handler.RoleInfoDataProvideHandler;
import cn.easii.example.handler.UserInfoDataProvideHandler;
import cn.easii.example.model.Article;
import cn.easii.example.model.PermissionQueryReq;
import cn.easii.example.model.Role;
import cn.easii.example.model.Scene;
import cn.easii.example.model.User;
import cn.easii.example.model.UserQueryReq;
import cn.easii.relation.MapToBeanHandle;
import cn.easii.relation.core.InjectRelation;
import cn.easii.relation.core.JsonMapToBeanHandle;
import cn.easii.relation.core.function.InnerFunction;
import cn.easii.relation.msp.MapStructPlusMapToBeanHandle;
import cn.hutool.core.date.StopWatch;
import cn.hutool.core.lang.Assert;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PerformanceTest {

    private InjectRelation injectRelation;

    @Autowired
    private MapStructPlusMapToBeanHandle mapToBeanHandle;

    private Scene initScene() {
        final Scene scene = new Scene();
        scene.setUsername("admin");
        return scene;
    }

    private void recordExecute(InnerFunction innerFunction, String taskName) {
        StopWatch stopWatch = new StopWatch();
        for (int j = 0; j < 10; j++) {
            stopWatch.start(taskName + " : " + j);
            for (int i = 0; i < 1000000; i++) {
                innerFunction.invoke();
            }
            stopWatch.stop();
        }
        System.out.println(stopWatch.prettyPrint(TimeUnit.MILLISECONDS));
    }

    @Test
    public void performanceTest() {
        // 通过 json 转换
        injectRelation = new InjectRelation(new JsonMapToBeanHandle());
        sceneTest();

        // 通过 MapStructPlus 转换
        injectRelation = new InjectRelation(mapToBeanHandle);
        sceneTest();
    }

    public void sceneTest() {
        final Scene scene = initScene();
        // 场景一：参数和返回类型都一致，不需要转换
        recordExecute(() -> {
            injectRelation.injectRelation(scene, "sceneOneNickName");
        }, "scene one");
        // 场景二：参数类型相同，不需要转换，返回类型不同，需要获取其中的一个属性
        recordExecute(() -> {
            injectRelation.injectRelation(scene, "sceneTwoNickName");
        }, "scene two");
        // 场景三：参数类型不同，需要转换，返回类型相同
        recordExecute(() -> {
            injectRelation.injectRelation(scene, "sceneThreeNickName");
        }, "scene three");
        // 场景三：参数类型和返回类型都不相同
        recordExecute(() -> {
            injectRelation.injectRelation(scene, "sceneFourNickName");
        }, "scene four");
    }

}
