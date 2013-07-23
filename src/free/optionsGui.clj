(ns free.optionsGui
  (:use [seesaw.core :only [frame combobox button listen grid-panel]])) 

(defn create-options-gui [promise-a-choice]
  (let [coordinates (combobox  :model ["Cartesian coordinate" "Polar coordinate"])
        Submit (button :text "Submit")
        panel (grid-panel :rows 2 :hgap 2 :vgap 2 :items [coordinates Submit])
        frame (frame :title "Experimental options" :on-close :exit :visible? true :content panel)]
    
    (doto frame
      (.setLocation 500 300) 
      (.setSize 400 120)) 
    (listen Submit
            :action (fn [e] (doseq [] (.dispose frame)
                              (deliver promise-a-choice {:is-polar (case (.getSelectedIndex coordinates)
                                                                     0 false
                                                                     1 true)
                                                         :subject-id "Minos-KangWoo"
                                                         :start-trial 1}))))))

;;usage 
;(do 
;  (def is-polar1 (promise)) 
;  (create-options-gui is-polar1)
; @is-polar1)


