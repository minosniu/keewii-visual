(ns keewiiVisual.threads
  (:require [clojure.string :as string])
  (:use [keewiiVisual.udp]
        [keewiiVisual.basic_setting]
        [singleTarget.saveData :only (save-temp-data)]))
 
;UDP receving thread
(def agent-udp (agent nil))
(defn udp-receive []
  (dosync
    (let [time  (/ (- (now) @Time) 1000.0) ;in ms
          MSG (receive-msg)
          msg (map read-string (string/split MSG #":"))
          EMG1 (second (rest msg))
          EMG2 (last msg)]
      (reset! F1 (first msg))
      (reset! F2 (second msg))
      (save-temp-data time EMG1 EMG2 @loop-count)))); vowel-showed cursor-f1 cursor-f2
(defn udp-reception [x]       
  (when @running
    (udp-receive)
    (send-off *agent* #'udp-reception) 
    nil))
   ; vowel-showed cursor-f1 cursor-f2
  
