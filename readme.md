# ZlogViewer

日志查看器，浏览器实时展示服务器日志

![image-20200304154048176](https://tva1.sinaimg.cn/large/00831rSTly1gchxsyxcyqj31gb0k5n7v.jpg)



## 使用方式
### 下载
[Zlog-0.0.1](https://github.com/ZhaoYueNing/ZlogViewer/releases/tag/zlog-0.0.1)

### 编辑配置文件

```
vim ~/.zlog/config.json

{
  "logFiles": [
    {
      "sourceId": "DemoLog",
      "filePath": "/Users/zhaoyuening/.zlog/demo.log"
    },
    {
      "sourceId": "Demo2Log",
      "filePath": "/Users/zhaoyuening/.zlog/demo2.log"
    }
  ]
}
```

### 启动程序

```
nohup java -jar zlog-0.0.1-SNAPSHOT.jar --server.port=8080 &
```

### 访问浏览器

http://localhost:8080

![image-20200304154130467](https://tva1.sinaimg.cn/large/00831rSTly1gchxtoywjpj31h007umzq.jpg)
