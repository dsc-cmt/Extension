import org.apache.zookeeper.server.ServerConfig;
import org.apache.zookeeper.server.ZooKeeperServerMain;
import org.apache.zookeeper.server.quorum.QuorumPeerConfig;

import java.io.File;
import java.util.Properties;

/**
 * @author xieyong
 * @date 2020/3/30
 * @Description:
 */
public class LocalZooKeeperServer {
    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        props.setProperty("tickTime", "2000");
        //需要
        props.setProperty("dataDir", new File(System.getProperty("java.io.tmpdir"), "zookeeper").getAbsolutePath());
        props.setProperty("clientPort", "2181");
        props.setProperty("initLimit", "10");
        props.setProperty("syncLimit", "5");

        QuorumPeerConfig quorumConfig = new QuorumPeerConfig();
        try {
            quorumConfig.parseProperties(props);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }

        final ZooKeeperServerMain zkServer = new ZooKeeperServerMain();
        final ServerConfig config = new ServerConfig();
        config.readFrom(quorumConfig);
        zkServer.runFromConfig(config);
    }
}
