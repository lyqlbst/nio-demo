# nio-demo
a nio demo project

> 包含io、nio、netty的一些例子，帮助理解nio

---

#### ```io.classic```

> 说明：
>
>模拟了同步阻塞（BIO）模型，服务端在接收到客户端的请求后，每一次都会创建一个线程去处理。
>
> 缺陷：
>
> 性能低下，无法满足高并发场景。
