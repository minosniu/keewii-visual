(ns keewiiVisual.udp
  (:import (java.net DatagramPacket DatagramSocket InetAddress)))

;UDP- server Port localhost:9001 
(defn receive-data [receive-socket]
  "Packet parsing"
  (let [receive-data (byte-array 1024),
       receive-packet (new DatagramPacket receive-data 1024)]
  (.receive receive-socket receive-packet)
  (new String (.getData receive-packet) 0 (.getLength receive-packet))))
(def receive-msg
  (let [receive-socket (DatagramSocket. 9001)]
		(fn [] (receive-data receive-socket))))

; Only in case wav file is not recorded
(defn make-socket 
   	([] (new DatagramSocket))
   	([port] (new DatagramSocket port)))
(defn send-data [send-socket ip port data]
     (let [ipaddress (InetAddress/getByName ip),
           send-packet (new DatagramPacket (.getBytes data) (.length data) ipaddress port)]
     (.send send-socket send-packet)))
 (defn make-send [ip port]
   	(let [send-socket (make-socket)]
   	     (fn [data] (send-data send-socket ip port data))))
