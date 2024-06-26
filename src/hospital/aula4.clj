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


;quem define qual sera a estrategia.
(defmulti deve-assinar-pre-autorizacao-multi? class)
;invoca a funcao
(defmethod deve-assinar-pre-autorizacao-multi? PacienteParticular [paciente] false)
(defmethod deve-assinar-pre-autorizacao-multi? PacientePlanoDeSaude [paciente] false)




(let [particular (->PacienteParticular 15 "Luana" "15/02/1996", :normal)
      plano (->PacientePlanoDeSaude 15 "Luana" "15/02/1996", :normal, [:raio-x, :ultrasom])]
  (pprint (deve-assinar-pre-autorizacao-multi? particular))
  (pprint (deve-assinar-pre-autorizacao-multi? plano)))


;pedido (:paciente paciente, :valor valor, :procedimento procedimentp}


;como funciona a funcao que define a estrategia de um defmulti
(defn minha-funcao [p]
  (println p)
  (class p))

(defmulti multi-teste minha-funcao)

;(multi-teste :luana)


(defn tipo-do-autorizador [pedido]
  (let [paciente (:paciente pedido)
        situacao (:situacao paciente)
        urgencia? (= :urgente situacao)]
    (if urgencia?
      :sempre-autorizado
      (class paciente))))

(defmulti deve-assinar-pre-autorizacao-do-pedido? tipo-do-autorizador)

(defmethod deve-assinar-pre-autorizacao-do-pedido? :sempre-autorizado [pedido] false)
(defmethod deve-assinar-pre-autorizacao-do-pedido? PacienteParticular [pedido] (>= (:valor pedido 0) 50))
(defmethod deve-assinar-pre-autorizacao-do-pedido? PacientePlanoDeSaude [pedido] (not (some #(= % (:procedimento pedido)) (:plano (:paciente pedido)))))



(let [particular (->PacienteParticular 15 "Luana" "15/02/1996", :urgente)
      plano (->PacientePlanoDeSaude 15 "Luana" "15/02/1996", :urgente, [:raio-x, :ultrasom])]
  (pprint (deve-assinar-pre-autorizacao-do-pedido? {:paciente particular, :valor 1000, :procedimento :raio-x}))
  (pprint (deve-assinar-pre-autorizacao-do-pedido? {:paciente plano, :valor 1000, :procedimento :raio-x})))


(let [particular (->PacienteParticular 15 "Luana" "15/02/1996", :normal)
      plano (->PacientePlanoDeSaude 15 "Luana" "15/02/1996", :normal, [:raio-x, :ultrasom])]
  (pprint (deve-assinar-pre-autorizacao-do-pedido? {:paciente particular, :valor 1000, :procedimento :raio-x}))
  (pprint (deve-assinar-pre-autorizacao-do-pedido? {:paciente plano, :valor 1000, :procedimento :coleta-de-sangue})))









