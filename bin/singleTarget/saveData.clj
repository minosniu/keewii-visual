(ns singleTarget.saveData
  (:require [singleTarget.initializing :as init]
            [clojure.java.io :as io]))

(defn copy-file [source-path dest-path]
  (io/copy (io/file source-path) (io/file dest-path)))

(defn save-temp-wav [recorder-path temp-path session-number]
  "This function passes a command to record a wave file by sox-14-4-1 program"
  (str "cmd /c " recorder-path " -c 2 " temp-path (format "%02d" session-number) ".wav trim 0 5"))

(defn save-temp-data [path session-number]
  "This function gets a path of temp folder and session number to make temp files of data files" 
  (let [sequence-index (- session-number 1)] 
    (spit (str path (format "%02d" session-number) ".txt") 
          (str "\n" ((init/randomized-sequence sequence-index) :name) "\n") :append true )))

(defn save-data-file [path-temp path-data session-number]
  (let []
    (copy-file (str path-temp (format "%02d" session-number) ".wav") (str path-data "\\wav\\" (format "%02d" session-number) ".wav"))
    (copy-file (str path-temp (format "%02d" session-number) ".txt") (str path-data "\\dat\\" (format "%02d" session-number) ".dat")))) 

