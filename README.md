#Redis 反向索引技术

#redis主从集群配置

参考博客：https://www.cnblogs.com/linuxbug/p/5131504.html

    zune@zune:~/桌面/redis$ tree
    .
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
    │   ├── sentinel2
    │   │   ├── sentinel2.conf
    │   │   └── sentinel2.log
    │   └── sentinel3
    │       ├── sentinel3.conf
    │       └── sentinel3.log
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
    
    
查看主节点

    ./redis-cli -p 26371
    127.0.0.1:26371> SENTINEL masters
    1)  1) "name"
        2) "TestMaster"
        3) "ip"
        4) "127.0.0.1"
        5) "port"
        6) "7005"
        7) "runid"
        8) "2d5ac727f69083b2ef70ea0a6a910f6d270ee5ee"
        9) "flags"
       10) "master"
       11) "link-pending-commands"
       12) "0"
       13) "link-refcount"
       14) "1"
       15) "last-ping-sent"
       16) "0"
       17) "last-ok-ping-reply"
       18) "625"
       19) "last-ping-reply"
       20) "625"
       21) "down-after-milliseconds"
       22) "1500"
       23) "info-refresh"
       24) "4967"
       25) "role-reported"
       26) "master"
       27) "role-reported-time"
       28) "75201"
       29) "config-epoch"
       30) "8395"
       31) "num-slaves"
       32) "3"
       33) "num-other-sentinels"
       34) "4"
       35) "quorum"
       36) "1"
       37) "failover-timeout"
       38) "10000"
       39) "parallel-syncs"
       40) "1"
    
查看从节点
    
    127.0.0.1:26371> SENTINEL slaves TestMaster
    1)  1) "name"
        2) "127.0.0.1:7003"
        3) "ip"
        4) "127.0.0.1"
        5) "port"
        6) "7003"
        7) "runid"
        8) "da2bfbb256dbaea3ebf4eea3858977d3c5b2cb60"
        9) "flags"
       10) "slave"
       11) "link-pending-commands"
       12) "0"
       13) "link-refcount"
       14) "1"
       15) "last-ping-sent"
       16) "0"
       17) "last-ok-ping-reply"
       18) "705"
       19) "last-ping-reply"
       20) "705"
       21) "down-after-milliseconds"
       22) "1500"
       23) "info-refresh"
       24) "8196"
       25) "role-reported"
       26) "slave"
       27) "role-reported-time"
       28) "319330"
       29) "master-link-down-time"
       30) "0"
       31) "master-link-status"
       32) "ok"
       33) "master-host"
       34) "127.0.0.1"
       35) "master-port"
       36) "7005"
       37) "slave-priority"
       38) "100"
       39) "slave-repl-offset"
       40) "62379"
    2)  1) "name"
        2) "127.0.0.1:7004"
        3) "ip"
        4) "127.0.0.1"
        5) "port"
        6) "7004"
        7) "runid"
        8) "2a4f1ed1ed425591849fc9ec7290d2b626161b61"
        9) "flags"
       10) "slave"
       11) "link-pending-commands"
       12) "0"
       13) "link-refcount"
       14) "1"
       15) "last-ping-sent"
       16) "0"
       17) "last-ok-ping-reply"
       18) "705"
       19) "last-ping-reply"
       20) "705"
       21) "down-after-milliseconds"
       22) "1500"
       23) "info-refresh"
       24) "8196"
       25) "role-reported"
       26) "slave"
       27) "role-reported-time"
       28) "319330"
       29) "master-link-down-time"
       30) "0"
       31) "master-link-status"
       32) "ok"
       33) "master-host"
       34) "127.0.0.1"
       35) "master-port"
       36) "7005"
       37) "slave-priority"
       38) "100"
       39) "slave-repl-offset"
       40) "62379"
    3)  1) "name"
        2) "127.0.0.1:7006"
        3) "ip"
        4) "127.0.0.1"
        5) "port"
        6) "7006"
        7) "runid"
        8) "cfdf4a51956288983b5bc07f4de88b4c56aaaf5f"
        9) "flags"
       10) "slave"
       11) "link-pending-commands"
       12) "0"
       13) "link-refcount"
       14) "1"
       15) "last-ping-sent"
       16) "0"
       17) "last-ok-ping-reply"
       18) "705"
       19) "last-ping-reply"
       20) "705"
       21) "down-after-milliseconds"
       22) "1500"
       23) "info-refresh"
       24) "8196"
       25) "role-reported"
       26) "slave"
       27) "role-reported-time"
       28) "319330"
       29) "master-link-down-time"
       30) "0"
       31) "master-link-status"
       32) "ok"
       33) "master-host"
       34) "127.0.0.1"
       35) "master-port"
       36) "7005"
       37) "slave-priority"
       38) "100"
       39) "slave-repl-offset"
       40) "62379"