import socketserver
import threading
from queue import Queue
import time
import serial


result = Queue()
# 创建锁
mutex = threading.Lock()
event = threading.Event()
Flag = False


class PySocketServerHandler(socketserver.BaseRequestHandler):
    def handle(self):
        global Flag
        print('Got connection from', self.client_address)
        while True:
            msg = self.request.recv(1024)
            msg = msg.decode()
            if str(msg) == 'start':
                t1 = threading.Thread(target=reciveData)
                t1.start()
                while True:
                    if Flag:
                        print("OK")
                        self.request.send("OK\n".encode("utf-8"))
                        break
                    time.sleep(1)
            elif str(msg) == 'read':
                print("收到read")
                while True:
                    if not result.empty():
                        mutex.acquire()
                        data = result.get()
                        mutex.release()
                        ret = str(data+'\n').encode("utf-8")
                        print(data)
                        self.request.send(ret)
                    time.sleep(10)
            else:
                exit(1)


def reciveData():
    global Flag
    try:
        ser = serial.Serial('COM4', 960, timeout=0.5)
        if ser.is_open:
            print("打开串口成功！")
            output = open('ADS_B_data.txt', 'w')
            Flag = True
            while True:
                n = ser.in_waiting
                if n:
                    data = ser.readlines()
                    for s in range(0, len(data)):
                        redata = data[s].decode(encoding="utf-8").split('\r')[0]
                        print(redata)
                        output.write(redata)
                        mutex.acquire()
                        result.put(redata)
                        mutex.release()
                time.sleep(10)
            output.close()
    except Exception as e:
        print("---异常---：", e)
    # serial.close()


if __name__ == '__main__':
    serv = socketserver.ThreadingTCPServer(('localhost', 20000), PySocketServerHandler)
    serv.serve_forever()