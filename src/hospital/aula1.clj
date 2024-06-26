(ns hospital.aula1
  (:use clojure.pprint))


(defn adiciona-paciente
  "Os Pacientes sao um mapa na forma {15 {paciente 15}, 23 {paciente 23}}
    O paciente eh {:id 15 ......}"
  [pacientes paciente]
  (if-let [id (:id paciente)]
    (assoc pacientes id paciente)
    (throw (ex-info "Paciente nao possui id" {:paciente paciente}))))


(defn testa-uso-de-pacientes []
  (let [pacientes {}
        luana {:id 15, :nome "Luana", :nascimento "15/02/1996"}
        ailin {:id 20, :nome "Ailin", :nascimento "10/02/1989"}
        tais { :nome "Tais", :nascimento "04/06/1992"}]

    (pprint (adiciona-paciente pacientes luana))
    (pprint (adiciona-paciente pacientes ailin))
    (pprint (adiciona-paciente pacientes tais))
    ))


;(testa-uso-de-pacientes)

;numa VM java o Paciente eh classe
(defrecord Paciente [id, nome, nascimento])

(println (->Paciente 15 "Luana" "15/02/1996"))
(pprint (->Paciente 15 "Luana" "15/02/1996"))
;o . significa que invocara um construtor de uma classe
(pprint (Paciente. 15 "Luana" "15/02/1996"))

(pprint (map->Paciente {:id 15, :nome "Luana", :nascimento "15/02/1996"}))

(let [luana (->Paciente 15 "Luana" "15/02/1996")]
  (println (:id luana))
  (println (vals luana)))





