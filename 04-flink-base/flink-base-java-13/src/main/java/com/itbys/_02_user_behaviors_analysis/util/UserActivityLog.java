package com.itbys._02_user_behaviors_analysis.util;

import com.itbys._02_user_behaviors_analysis.bean.MarketingUserBehavior;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Author xx
 * Date 2023/8/5
 * Desc
 */
public class UserActivityLog implements SourceFunction<MarketingUserBehavior> {

    // 是否运行的标识位
    Boolean running = true;
    // 定义用户行为和渠道的集合
    List<String> behaviorList = Arrays.asList("CLICK", "DOWNLOAD", "INSTALL", "UNINSTALL");
    List<String> channelList = Arrays.asList("app store", "weibo", "wechat", "tieba");
    Random random = new Random();

    @Override
    public void run(SourceContext<MarketingUserBehavior> sourceContext) throws Exception {
        while (running) {
            Long id = random.nextLong();
            String behavior = behaviorList.get(random.nextInt(behaviorList.size()));
            String channel = channelList.get(random.nextInt(channelList.size()));
            long millis = System.currentTimeMillis();

            sourceContext.collect(new MarketingUserBehavior(id, behavior, channel, millis));

            Thread.sleep(100L);
        }
    }

    @Override
    public void cancel() {
        running = false;
    }
}
