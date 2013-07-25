(ns singleTarget.saveData
  (:require [singleTarget.initializing :as init]
            [clojure.java.io :as io])
  (:use [keewiiVisual.toolbox :only (cur_dir)]
        [keewiiVisual.basic_setting :only [F1 F2]]))

(defn copy-file [source-path dest-path]
  (io/copy (io/file source-path) (io/file dest-path)))

(defn save-temp-wav [recorder-path temp-path session-number]
  "This function passes a command to record a wave file by sox-14-4-1 program"
  (str "cmd /c " recorder-path " -c 2 " temp-path (format "%02d" session-number) ".wav trim 0 5"))

(defn save-temp-seq [temp-path session-number]
  "This function gets a path of temp folder and session number to make temp files of data files" 
  (let [sequence-index (- session-number 1)] 
    (spit (str temp-path (format "%02d" session-number) ".txt") 
          (str "\n" ((init/randomized-sequence sequence-index) :name) "\n") :append true )))
(defn save-temp-data [time EMG1 EMG2 session-number]
  (let [temp-path (str (cur_dir) "\\temp\\")] 
    (when (and (and (>= @F1 0) (<= @F1 1000 )) (and (>= @F2 200 ) (<= @F2 3000 )) (and (>= time 0.0) (<= time 5.0)))
      (spit (str temp-path (format "%02d" session-number) ".txt") (str time " " EMG1 " " EMG2 " " @F1 " " @F2 "\n")  :append true)))); vowel-showed cursor-f1 cursor-f2

(defn save-data-file [path-temp path-data session-number]
  (let []
    (copy-file (str path-temp (format "%02d" session-number) ".wav") (str path-data "\\wav\\" (format "%02d" session-number) ".wav"))
    (copy-file (str path-temp (format "%02d" session-number) ".txt") (str path-data "\\dat\\" (format "%02d" session-number) ".dat")))) 

