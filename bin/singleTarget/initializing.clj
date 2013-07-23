(ns singleTarget.initializing
  (:require [clojure.string :as string]
            [keewiiVisual.toolbox :as toolbox]
            [clojure.java.io :as io])
  (:use [keewiiVisual.vowelList]))

;File & Directory info
(defn datapath [{subject-id :subject-id is-polar :is-polar}]
  (str (toolbox/cur_dir) "\\data\\" subject-id "\\" (if is-polar "Polar" "Cart") "\\")) 
(defn mkdir-all [all-options]
  (let [FILENAME (datapath all-options)] 
    (toolbox/mkdir (str (toolbox/cur_dir) "\\data\\")) 
    (toolbox/mkdir (str (toolbox/cur_dir) "\\temp\\"))
    (toolbox/mkdir FILENAME)
    (toolbox/mkdir (str FILENAME "\\dat\\"))
    (toolbox/mkdir (str FILENAME "\\wav\\"))))

(defn seq-exist? [fname]
  "This function checks if seq.txt exists" 
  (let [f (io/file fname)]
    (cond
      (.isFile f)      (def randomized-sequence 
                         (into [] (map #(toolbox/str2vowel %) (string/split (slurp fname) #"\s+"))) );"file"
      (.isDirectory f) "directory"
      (.exists f)      "other"
      :else            (doseq [] (def randomized-sequence  ;non existent case
                                   (shuffle (reduce into (map #(repeat 5 %) [A E I O U])))) 
                         (spit fname (string/join " " (reduce str (map #(% :name) randomized-sequence))))))))