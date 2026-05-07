# Docker 本地中间件连接信息

当前本地 Docker 容器启动后，可使用下面地址访问或连接。

## 浏览器访问地址

| 服务 | 地址 | 账号/密码 |
| --- | --- | --- |
| Nacos | http://localhost:8848/nacos | nacos / nacos |
| RabbitMQ Management | http://localhost:15672 | guest / guest |
| Sentinel Dashboard | http://localhost:8858 | sentinel / sentinel |
| Seata Console | http://localhost:7091 | 默认无 |

## 后端连接地址

| 服务 | 地址 | 说明 |
| --- | --- | --- |
| MySQL | localhost:3306 | root / root |
| Redis | localhost:6379 | 无密码 |
| RabbitMQ | localhost:5672 | guest / guest |
| Nacos | localhost:8848 | 服务注册与配置 |
| Sentinel | localhost:8858 | 控制台地址 |
| Seata | localhost:8091 | TC 服务端口 |

## 常用检查命令

```powershell
docker ps
docker logs -f payment-nacos
docker logs -f payment-rabbitmq
docker logs -f payment-sentinel
docker logs -f payment-seata
docker exec -it payment-redis redis-cli ping
docker exec -it payment-mysql mysql -uroot -proot
```

## 常用启动命令

```powershell
docker start payment-mysql
docker start payment-redis
docker start payment-rabbitmq
docker start payment-nacos
docker start payment-sentinel
docker start payment-seata
```
