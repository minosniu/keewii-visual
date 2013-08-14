(ns keewiiVisual.threads
  (:require [clojure.string :as string])
  (:use [keewiiVisual.udp]
        [keewiiVisual.basic_setting]
        [singleTarget.saveData :only (save-temp-data)]))
 
; (def agent-drawing (agent nil))
; (defn re-drawing [x]
;  (when true;@running
;     (send-off *agent* #'re-drawing))
;   (.repaint panel)
;   (Thread/sleep re-drawing-sleep-ms) nil) ;also need to define panel
 
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
      ;(println @F1 @F2 "\n")
      (save-temp-data time EMG1 EMG2 @loop-count)))); vowel-showed cursor-f1 cursor-f2
(defn udp-reception [x]       
    (udp-receive)
    (send-off *agent* #'udp-reception) 
    
  )
   ; vowel-showed cursor-f1 cursor-f2
  
