(ns singleTarget.optionsGui
  (:use [keewiiVisual.toolbox :only (str2int)]
        [seesaw.core :only [frame text combobox button listen grid-panel]])
  (:require [seesaw.behave :as b])) 

(defn create-options-gui [promise-a-choice]
  (let [coordinates (combobox  :model ["Cartesian coordinate" "Polar coordinate"]) 
        Name (text :text "Type your name here." :tip "Name")
        Start (text :text "Set the trial number to start from. (1~25)" :tip "Starting point") 
        Submit (button :text "Submit") 
        panel (grid-panel :rows 4 :hgap 2 :vgap 2 :items [coordinates Name Start Submit]) 
        frame (frame :title "Experimental options" :on-close :exit :visible? true :content panel)]
    
    (doto Start b/when-focused-select-all)
    (doto Name b/when-focused-select-all)  
    (doto frame
      (.setLocation 500 300) 
      (.setSize 400 300))
    (listen Submit
            :action (fn [e] (doseq [] 
                              (.dispose frame)
                              (deliver promise-a-choice {:is-polar (case (.getSelectedIndex coordinates)
                                                                     0 false
                                                                     1 true)
                                                         :subject-id (.getText Name)
                                                         :start-trial (str2int (.getText Start))}))))))
;; usage
;(do 
;  (def all-options (promise)) 
;  (create-options-gui all-options)
;  @all-options)