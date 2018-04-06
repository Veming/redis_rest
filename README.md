#Redis 反向索引技术

#redis主从集群配置
    ├── master
    │   ├── dump.rdb
    │   ├── redis7003.log
    │   ├── redis-cli
    │   ├── redis.config
    │   └── redis-server
    ├── sentinel
    │   ├── redis-cli
    │   ├── redis-sentinel
    │   ├── redis-server
    │   ├── sentinel1
    │   │   ├── sentinel1.conf
    │   │   └── sentinel1.log
    │   ├── sentinel1.log
    │   ├── sentinel2
    │   │   ├── sentinel2.conf
    │   │   └── sentinel2.log
    │   ├── sentinel2.log
    │   ├── sentinel3
    │   │   ├── sentinel3.conf
    │   │   └── sentinel3.log
    │   └── sentinel3.log
    └── slave
    
        ├── 7004
        │   ├── dump.rdb
        │   ├── redis7004.log
        │   ├── redis-cli
        │   ├── redis.config
        │   └── redis-server
        ├── 7005
        │   ├── dump.rdb
        │   ├── redis7005.log
        │   ├── redis-cli
        │   ├── redis.config
        │   └── redis-server
        └── 7006
            ├── dump.rdb
            ├── redis7006.log
            ├── redis-cli
            ├── redis.config
            └── redis-server
        
数据库密码 ：0234kz9*l 
