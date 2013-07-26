(ns singleTarget.core
  (:require [clojure.string :as string]
            [clojure.java.io :as io]
            [singleTarget.initializing :as init]
            [singleTarget.saveData :as saveData])
  (:use [keewiiVisual.basic_setting] 
        [keewiiVisual.vowelList]
        [keewiiVisual.toolbox]
        [singleTarget.displayGui]
        [singleTarget.optionsGui] 
        [keewiiVisual.threads]
        [seesaw.core])) 

;; GUI for paradigm, subj id, start trial...
(do 
  (def all-options (promise)) 
  (create-options-gui all-options)
  @all-options)

;;make directoriesfor data
(init/mkdir-all @all-options)

;; GUI for experiment & fre-exploration
(def-keewii []
  (let [painter (partial render-vowel-map @all-options )
        cvs (canvas :id :canvas :background "#BBBBBB" :paint painter)]
    (frame 
      :title "Experiment" 
      :width (dim 0) :height (dim 1)
      :content cvs
      :on-close :exit
      :minimum-size [(dim 0) :by (dim 1)])))
(run :dispose)
  
;UDP thread
(send-off agent-udp udp-reception)
(Thread/sleep Delayed-start);Starting pause

;; Sequence check
(init/seq-exist? (str (init/datapath @all-options) "seq.txt")) ;If exists, we will use pre-defined sequence

;; Main loop KTH + data logging
(doseq [i (range  (- (@all-options :start-trial) 1) total-trials)]
  (while (not @running)) 
  (reset! loop-count (+ i 1)) 
  (let [session-number @loop-count
        temp-path (str (cur_dir) "\\temp\\")
        recording-info (saveData/save-temp-wav sox-path temp-path session-number)
        data-path (init/datapath @all-options)]
    
    (doseq [] (reset! CUR_TGT_sleep true) (reset! Time (now)))
    (sox-recorder recording-info) ;sound recorder on
    (KTH-operator true) ;KTH synthesizer on
    (reset! alphabet (init/randomized-sequence i)) ;change alphabet
    (saveData/save-temp-seq temp-path session-number) ; save current alphabet (right answer)
    (Thread/sleep  trial-duration)
    (KTH-operator false)
    (reset! CUR_TGT_sleep false) 
    (Thread/sleep between-trial-duration)
    (saveData/save-data-file temp-path data-path session-number)))
(quit)
