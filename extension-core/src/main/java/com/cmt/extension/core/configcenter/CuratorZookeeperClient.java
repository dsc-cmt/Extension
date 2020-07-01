package com.cmt.extension.core.configcenter;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;

/**
 * @author tuzhenxian
 * @date 20-6-29
 */
@Slf4j
public class CuratorZookeeperClient {
    private static final CuratorZookeeperClient INSTANCE = new CuratorZookeeperClient();
    private static final String CHARSET = "UTF-8";
    private CuratorFramework client;

    private CuratorZookeeperClient() {
    }

    public static CuratorZookeeperClient instance() {
        INSTANCE.init();
        return INSTANCE;
    }

    private  CuratorFramework newClient() {
        Properties properties=new Properties();
        String location =null;
        try(InputStream in=this.getClass().getClassLoader().getResourceAsStream("application.properties")) {
            properties.load(in);
            location =properties.getProperty("spi.config.center");
        } catch (IOException e) {
            e.printStackTrace();
        }
        int sleepMsBetweenRetries = 100;
        int maxRetries = 3;
        if(location==null){
            log.error("spi配置中心地址不可为空,请先配置");
        }
        RetryPolicy retryPolicy = new RetryNTimes(
                maxRetries, sleepMsBetweenRetries);
        CuratorFramework client = CuratorFrameworkFactory
                .newClient(location, retryPolicy);
        client.start();
        return client;
    }

    public void init() {
        if (client == null) {
            synchronized (this) {
                if (client == null) {
                    client = newClient();
                }
            }
        }
    }

    /**
     * create ZK Persistent Node
     *
     * @param path
     * @return
     */
    public void createPersistent(String path) {
        try {
            client.create().forPath(path);
        } catch (KeeperException.NodeExistsException e) {
            log.warn("ZNode " + path + " already exists.", e);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public void createPersistent(String path,String data) {
        try {
            client.create().forPath(path,data.getBytes(CHARSET));
        } catch (KeeperException.NodeExistsException e) {
            try {
                client.setData().forPath(path, data.getBytes(CHARSET));
            } catch (Exception e1) {
                throw new IllegalStateException(e.getMessage(), e1);
            }
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
    /**
     * create ZK Ephemeral Node with data
     *
     * @param path
     * @param data
     * @return
     */
    public void createEphemeral(String path, String data) {
        try {
            client.create().withMode(CreateMode.EPHEMERAL).forPath(path, data.getBytes(CHARSET));
        } catch (KeeperException.NodeExistsException e) {
            try {
                client.setData().forPath(path, data.getBytes(CHARSET));
            } catch (Exception e1) {
                throw new IllegalStateException(e.getMessage(), e1);
            }
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public void createEphemeral(String path) {
        try {
            client.create().withMode(CreateMode.EPHEMERAL).forPath(path);
        } catch (KeeperException.NodeExistsException e) {
            deletePath(path);
            createEphemeral(path);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public void deletePath(String key) {
        try {
            client.delete().deletingChildrenIfNeeded().forPath(key);
        } catch (KeeperException.NoNodeException e) {
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public boolean checkExists(String path) {
        try {
            if (client.checkExists().forPath(path) != null) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public String doGetContent(String path) {
        try {
            byte[] dataBytes = client.getData().forPath(path);
            return (dataBytes == null || dataBytes.length == 0) ? null : new String(dataBytes, CHARSET);
        } catch (KeeperException.NoNodeException e) {
            // ignore NoNode Exception.
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        return null;
    }

    public List<String> getChildren(String path) {
        try {
            return client.getChildren().forPath(path);
        } catch (KeeperException.NoNodeException e) {
            return null;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public Map<String, String> getChildrenContent(String path) {
        try {
            List<String> childNodes = client.getChildren().forPath(path);
            if (childNodes == null) {
                return null;
            }
            Map<String, String> map = new HashMap<>();
            for (String child : childNodes) {
                map.put(child, doGetContent(path + "/" + child));
            }
            return map;
        } catch (KeeperException.NoNodeException e) {
            return null;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public void doClose() {
        client.close();
    }

    public CuratorFramework getClient() {
        return client;
    }
}
