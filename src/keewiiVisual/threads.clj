(ns keewiiVisual.threads
  (:require [clojure.string :as string])
  (:use [keewiiVisual.udp]
        [keewiiVisual.vars]))
 
;UDP receving thread
(def agent-udp (agent nil))
(defn udp-receive []
  (dosync
    (let [time  (/ (- (now) @Time) 1000.0) ;in ms
          MSG (receive-msg)
          msg (map read-string (string/split MSG #":"))]
      (reset! F1 (first msg))
      (reset! F2 (second msg))
      (reset! EMG1 (second (rest msg)))
      (reset! EMG2 (last msg))))); vowel-showed cursor-f1 cursor-f2
(defn udp-reception [x]       
  (when @running
    (udp-receive)
    (send-off *agent* #'udp-reception) 
    nil))

