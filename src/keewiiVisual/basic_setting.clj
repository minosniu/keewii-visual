;You can change default setting here.
(ns keewiiVisual.basic_setting
  (:require [seesaw.font :as f]))

"Custom variables: Can be changed"
(def dim [1050 600]) ;dimension size only when we restore down the window
(def formant_lim [0 900 0 3000]);Formant limitation= [f1_min f1_max f2_min f2_max] 
;trial timing setting (ms)
(def re-drawing-sleep-ms 16) ;repainting interval
(def Delayed-start 5000) ;delay before the start
(def trial-duration 6000) ; trial duration for experiment
(def free-trial-duration 600000) ;trial duration for free-exploration
(def between-trial-duration 5000) ;trial interval 
(def total-trials 25) ;25 trials
;font
(def Font (f/font :size 100 :name "Arial unicode MS" :style :plain))
;directory setting 
(def sox-path "c:\\sox-14-4-1\\rec.exe") ;directory and file name of sox14-4-1 recording program
(def wish-path "C:\\Code\\emg_speech_local\\speech_5vowels.tcl") ;directory of ActiveTcl wish.exe


"Do not change this below"
(defn now
  "Return the current time in ms (from overtone-at-at lib)"
  [] (System/currentTimeMillis))
;log data in logfile
(def Time (atom (now)))
(def F1 (atom 210)) 
(def F2 (atom 510))
(def loop-count (atom 0)) 
;thread running?
(def running (atom true))
;pause 
(def CUR_TGT_sleep (atom true)) ;It hides cursor and target during interval
