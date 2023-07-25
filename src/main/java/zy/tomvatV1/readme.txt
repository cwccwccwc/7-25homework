版 本一:http服务器，提供静态资源访问。
浏览器: http://localhost:8090/wowotuan/index.html显示wowotuan页面.

分析协议 :
请求部分: (浏览器自动实现)GET /wowotuan/index.html HTTP/1.1Referer: XXXX
Iser-Agent: Mozilla/5.0 (acintosh; Intel Mac 0S X 10 15 4) Applellebkit/537.36 (KHTM., like becko) chrome 80.0.3987.16
...。空行

服务器响应部分:HTTP/1.1 200 0K
Accept-Ranges: bytesContent-Length: 92174Content-Type: text/html
. . .空行响应实体(index.html的文本内容)

服务器功能:
1.接收客户端的请求解析出它请求的文件名( /Wowotuan/index.html )及相对路 ( d: IdeaProjects yc119_net webapps2。查找这个文件是否存在，不存在-> 404页面存在 ->
1) 读取这个资源2) 构建响应协议Content-Type: 浏览器根据响应中的 Content-Type来决定使用什么引擎来解析数据text/html: html  -> html洁染cssCSS引|警js:js引擎图片(暗:图片引擎。
/wowotuan/index.html