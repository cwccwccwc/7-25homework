下载步骤：
1.先只取文件大小，创建本地空文件
2.在按线程分段下载

对于步骤一：Http协议
请求头->method->GET/POST/
HEAD/DELETE/PUT/TRACE/OPTION
    采用HEAD->它告诉服务器 只取文件的 Content-Length 不下载内容


 创建本地空文件，RandomAccessFile：随机访问文件
 （可访问文件的任意位置）|顺序访问
 raf.seek(字节数)    setLength(大小)

 计算每段大小  下载的位置如何通知服务器
 Range.bytes=start-end

 如何在主线程中获知每个子线程的下载量，并累加.
 -> 回调机制+synchronized/AtomicLong