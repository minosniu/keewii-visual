(ns singleTarget.core
  (:require [clojure.string :as string]
            [clojure.java.io :as io]
            [singleTarget.initializing :as init]
            [singleTarget.saveData :as saveData])
  (:use [keewiiVisual.vars] 
        [keewiiVisual.vowelList]
        [keewiiVisual.toolbox]
        [singleTarget.displayGui]
        [singleTarget.optionsGui] 
        [keewiiVisual.threads])
  (:import (java.awt Dimension)
           (javax.swing JPanel JFrame)
           (java.lang.Runtime)))

;; GUI for paradigm, subj id, start trial...
(do 
  (def all-options (promise)) 
  (create-options-gui all-options)
  @all-options)

;;make directoriesfor data
(init/mkdir-all @all-options)

;;Draw panel and frame for 2D vowel map 
(def #^{:private true} frame)
(def ^JPanel panel (doto (proxy [JPanel] [] (paint [g] (render-vowel-map g @all-options frame)))
                     (.setPreferredSize (Dimension. (first dim) (last dim)))));only when we restore down the window
(def frame 
  (doto (JFrame.) 
    (.add panel) .pack .show (. setAlwaysOnTop true) 
    (. setExtendedState JFrame/MAXIMIZED_BOTH) 
    (.addKeyListener (input-listener))))

;; Start multi-threads for panel painting and UDP reception
(defn re-drawing [x]
  (when true
    (send-off *agent* #'re-drawing)
    (repaint-panel panel)
    (Thread/sleep re-drawing-sleep-ms) nil))
(send-off agent-drawing re-drawing)
(defn udp-reception [x]       
  (when @running
    (udp-receive)
    (send-off *agent* #'udp-reception) 
    nil))
(send-off agent-udp udp-reception)

(. Thread (sleep 5000));Starting pause
;; Sequence check
(init/seq-exist? (str (init/datapath @all-options) "seq.txt")) ;If exists, we will use pre-defined sequence

;; Main loop KTH + data logging
(doseq [i (range  (- (@all-options :start-trial) 1) total-trials)]
  ;(reset! session_number  (inc i)) 
  (while (not @running)) 
  (let [session-number (+ i 1)
        temp-path (str basic-path "temp\\")
        recording-info (saveData/save-temp-wav sox-path temp-path session-number)
        data-path (init/datapath @all-options)]
    (reset! CUR_TGT_sleep true)
    (reset! Time (now))
    (. (Runtime/getRuntime) exec recording-info)
    (. (Runtime/getRuntime) exec "wish C:\\Code\\emg_speech_local\\speech_5vowels.tcl")      
    (reset! alphabet (init/randomized-sequence i)) 
     ;It saves the right answer
    (saveData/save-temp-data temp-path session-number)
    (Thread/sleep  trial-duration)
    (. (Runtime/getRuntime) exec "taskkill /F /IM  wish.exe")
    (reset! CUR_TGT_sleep false) 
    (Thread/sleep between-trial-duration)
    (saveData/save-data-file temp-path data-path session-number)))
(System/exit 0)
