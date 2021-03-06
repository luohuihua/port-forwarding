var interval_time = 60000;

layui.use(['layer', 'table', 'jquery'], function () {

// Socket
    function gc() {
        var socket = new SockJS('/port-forwarding/websocket');
        var stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            stompClient.subscribe('/topic/gc', function (d) {
                var data = JSON.parse(d.body)
                s0(data)
                s1(data)
                eden(data)
                old(data)
                mc(data)
                ccsc(data)
                gcn(data)
                gct(data)
                setTimeout(function () {
                    stompClient.send("/app/gc", {}, GetQueryString("pid"));
                }, interval_time);
            });
            stompClient.send("/app/gc", {}, GetQueryString("pid"));
        });
    }

    function cl() {
        var socket = new SockJS('/port-forwarding/websocket');
        var stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            stompClient.subscribe('/topic/cl', function (d) {
                var data = JSON.parse(d.body)
                classn(data)
                classt(data)
                comn(data)
                comt(data)
                combyte(data)
                setTimeout(function () {
                    stompClient.send("/app/cl", {}, GetQueryString("pid"));
                }, interval_time);
            });
            stompClient.send("/app/cl", {}, GetQueryString("pid"));
        });
    }

    function thread() {
        var socket = new SockJS('/port-forwarding/websocket');
        var stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            stompClient.subscribe('/topic/thread', function (d) {
                var data = JSON.parse(d.body)
                threadData(data);
                setTimeout(function () {
                    stompClient.send("/app/thread", {}, GetQueryString("pid"));
                }, interval_time);
            });
            stompClient.send("/app/thread", {}, GetQueryString("pid"));
        });
    }

//?????????-????????????
    layer.load();

//??????
    gc()
    cl()
    thread()

//??????????????????
    setTimeout(function () {
        layer.closeAll('loading');
    }, 2000);

//????????????
    $("#pinlv").click(function () {
        //prompt???
        layer.prompt({title: '???????????????????????????,??????/???'}, function (pass, index) {
            layer.close(index);
            interval_time = pass * 1000;
            layer.msg('??????????????????????????????' + pass + '???')
        });
    });
});