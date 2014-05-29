(ns singleTarget.WavRevival
  (:import java.io.File)
  (:require [clojure.string :as string]
            [clojure.java.io :as io]
            [singleTarget.initializing :as init]
            [singleTarget.saveData :as saveData]
            [keewiiVisual.udp :as udp])
  (:use [keewiiVisual.basic_setting] 
        [keewiiVisual.vowelList]
        [keewiiVisual.toolbox]
        [singleTarget.displayGui]
        [singleTarget.optionsGui] 
        [keewiiVisual.threads]
        [seesaw.core]))
;; manual setting
(def cur-sub (str (cur_dir) "\\data\\constest"))
(def dir-cart (str cur-sub "\\Cart\\dat\\") )
(def dir-polar (str cur-sub "\\Polar\\dat\\"))
(def sample_time 31.25) ; 160 signals in 5 sec
(def send-msg (udp/make-send "localhost" 9002))
;(def wav-info (promise))

(defn lazyseq2vec [LazySeq]
  "This function convert a lazy sequence to a vector "
  (vec (into-array LazySeq)))
(defn ListFiles [directory]
  "This function get directory as a string and lists files in the folder.
   The list is saved as a lazy sequence."
  ; rest removes ..(folder).
  (rest (for [file (file-seq (clojure.java.io/file directory))] (.getName file))))
(defn formant-data-load [datapath]
  "This is only used for specific case(formants)
   It will receive data file of keewii experiment and give a vector"
  (lazyseq2vec (rest (rest (string/split   (slurp datapath) #"\n")))))
(defn wavdata2udp [wavdata-info]
  "It will receive wavdata info from the file and change the format for UDP sender"
  (for [formant-line wavdata-info]  
   (let [single-line (string/split formant-line #"\s")
         msg (str (single-line 3) ":" (single-line 4) ":" (single-line 1) ":" (single-line 2))]
;   (println msg) 
;   (println wav-info) 
   (send-msg msg)
   (Thread/sleep sample_time))))
;(defn wavdata2udp [wavdata-info]
;  "It will receive wavdata info from the file and change the format for UDP sender"
;  (for [formant-line wavdata-info] (println formant-line)))
;(defn wavdata2udp [formant-line]
;  (println formant-line))

(def filelist-cart (lazyseq2vec (ListFiles dir-cart)))
(def filelist-polar (lazyseq2vec (ListFiles dir-polar)))

(send-off agent-udp udp-reception)

;(def agent-fn (agent nil))
;(defn fn-reception [wav-info]
;  (wavdata2udp wav-info);() ;function
;  (send-off *agent* #'fn-reception))
;(send-off agent-fn fn-reception)

;(Thread/sleep Delayed-start);Starting pause
;
;(doseq [i (range  0 total-trials)]
;  (reset! loop-count (+ i 1))
;  (let [session-number @loop-count
;        temp-path-cart (str (cur_dir) "\\temp\\Cart\\")
;        temp-path-polar (str (cur_dir) "\\temp\\Polar\\")
;        recording-info-cart (saveData/save-temp-wav sox-path temp-path-cart session-number)
;        recording-info-polar (saveData/save-temp-wav sox-path temp-path-polar session-number)
;        datapath-cart (str dir-cart (filelist-cart i)) ;path of one data file
;        datapath-polar (str dir-cart (filelist-polar i)) 
;        wavdata-info-cart (formant-data-load datapath-cart) ;all data in *.dat
;        wavdata-info-polar (formant-data-load datapath-polar)
;        ]
;    (doseq [] (reset! CUR_TGT_sleep true) (reset! Time (now)))
;    (sox-recorder recording-info-cart)
;    (KTH-operator true)
;    
;    (deliver wav-info wavdata-info-cart)
;    (wavdata2udp wav-info); send udp
;
;    
;    (Thread/sleep  trial-duration)
;    (KTH-operator false)
;    (reset! CUR_TGT_sleep false) 
;    (Thread/sleep 2000)
;    ))

;(wavdata2udp (formant-data-load (str dir-cart (filelist-cart 0))))
