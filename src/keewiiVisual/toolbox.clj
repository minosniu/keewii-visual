(ns keewiiVisual.toolbox
  (:use  [keewiiVisual.basic_setting]
         [seesaw.core :only [config! pack! show! invoke-now full-screen! listen]]) 
  (:require [clojure.string :as string]
            [clojure.java.io :as io]
            [keewiiVisual.vowelList :as vowelList]))

;;file&directory functions
(defn mkdir [path]
  (.mkdirs (io/file path)))
(defn cur_dir []
     (.getCanonicalPath (io/file ".")))

;;structure conversion
(defn str2vowel [CH]
  "This function converts char to vowel structure"
  (case CH
    "\u0251" vowelList/A
    "\u025B" vowelList/E
    "i" vowelList/I
    "\u0254" vowelList/O
    "u" vowelList/U))
 (defn str2int [s]
   (Integer. (re-find  #"\d+" s )))
 (defn formant2pixel [VOWEL Scale-factor ind]
  "resize the number of formants to fit into the screen by scale factor from the screen size"
  (let [SHIFT 0] ;move 25pixels with font 100, but cursor is also moved.
    (if (= ind 1)
    (int (+ (/ (* Scale-factor (- (VOWEL :f1)  (formant_lim 0)))  (- (formant_lim 1) (formant_lim 0))) SHIFT)) ;200 600 ->0 1000 (0~1000), 
    (int (- (/ (* Scale-factor (- (formant_lim 3) (VOWEL :f2))) (- (formant_lim 3) (formant_lim 2))) SHIFT)) ))); 2600 2100 -> 3000 3000 (0~3000)

;;GUI functions
(defn key-parser [ch]
  "key-listener for pause, and future use of others" 
  (when (= (str ch) "p") 
    (doseq [] (println "Pause: " @running) 
      (swap! running not)))) ;pause
(defmacro def-keewii
  "this function invokes a frame with some setting.
  1) windows always on top
  2) p key listener for pause" 
  [arg-vec & body]
  `(do
     (defn ~'run [on-close# & args#]
       (let [~arg-vec args#
             f# (invoke-now ~@body)]
         (config! f# :on-close on-close#)
         (when (= (java.awt.Dimension.) (.getSize f#))
           (pack! f#))
         (show! f#)
         (listen f#
            :key-pressed #(key-parser (.getKeyChar %)))
         ;(.setExtendedState f# javax.swing.JFrame/MAXIMIZED_BOTH) ; or (full-screen! f#)
         (.setAlwaysOnTop f# true))) 
     (defn ~'-main [& args#]
       (apply ~'run :exit args#))))
(defn buffered-image [width height]
  "It creates a buffered image with given width&height" 
  (java.awt.image.BufferedImage. width height java.awt.image.BufferedImage/TYPE_INT_ARGB))

;;external programs execution & quit
(defn KTH-operator [status]
  "It runs/stops KTH-synthesizer"
  (if status
    (. (java.lang.Runtime/getRuntime) exec (str "wish " wish-path))
    (. (java.lang.Runtime/getRuntime) exec "taskkill /F /IM  wish.exe")))
(defn sox-recorder [recording-info]
  "It runs sox recording program"
  (. (java.lang.Runtime/getRuntime) exec recording-info))
(defn quit []
  (System/exit 0))


