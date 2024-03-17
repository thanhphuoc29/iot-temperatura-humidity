// socketManager.js
import SockJS from 'sockjs-client';
import { Client } from '@stomp/stompjs';

let stompClient;
let url = 'http://localhost:8080/sensor';

export const getSocket = () => {
    if (!stompClient) {
        const socket = new SockJS(url);
        stompClient = new Client({
            webSocketFactory: () => socket,
            reconnectDelay: 5000,
        });

        stompClient.activate();
        console.log('init socket done!')
    }
    // Thêm sự kiện để ngắt kết nối khi trang web đóng
    window.addEventListener('beforeunload', () => {
        disconnectSocket();
    });
    return stompClient;
};

export const disconnectSocket = () => {
    if (stompClient && stompClient.connected) {
        stompClient.deactivate();
        stompClient = null; // Đặt lại biến socketInstance khi ngắt kết nối
        console.log('socket disconnect...')
    }
};
