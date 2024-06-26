(ns hospital.aula4
   (:use clojure.pprint))


(defrecord PacienteParticular [id, nome, nascimento, situacao])
(defrecord PacientePlanoDeSaude [id, nome, nascimento, situacao, plano])



(defn nao-eh-urgente? [paciente]
  (not= :urgente (:situacao paciente :normal)))


(defprotocol Cobravel
  (deve-assinar-pre-autorizacao? [paciente procedimento valor]))

(extend-type PacienteParticular
  Cobravel
  (deve-assinar-pre-autorizacao? [paciente, procedimento, valor]
    (and (>= valor 50) (nao-eh-urgente? paciente))))

(extend-type PacientePlanoDeSaude
  Cobravel
  (deve-assinar-pre-autorizacao? [paciente, procedimento, valor]
    (let [plano (:plano paciente)]
      (and (not (some #(= % procedimento) plano)) (nao-eh-urgente? paciente)))))

(let [particular (->PacienteParticular 15 "Luana" "15/02/1996", :normal)
      plano (->PacientePlanoDeSaude 15 "Luana" "15/02/1996", :normal, [:raio-x, :ultrasom])]
  (pprint (deve-assinar-pre-autorizacao? particular, :raio-x, 500))
  (pprint (deve-assinar-pre-autorizacao? plano, :raio-x, 500)))

(let [particular (->PacienteParticular 15 "Luana" "15/02/1996", :urgente)
      plano (->PacientePlanoDeSaude 15 "Luana" "15/02/1996", :urgente, [:raio-x, :ultrasom])]
  (pprint (deve-assinar-pre-autorizacao? particular, :raio-x, 500))
  (pprint (deve-assinar-pre-autorizacao? plano, :raio-x, 500)))


(defmulti deve-assinar-pre-autorizacao-multi? class)
(defmethod deve-assinar-pre-autorizacao-multi? PacienteParticular [paciente] )