(ns hospital.aula5
  (:use clojure.pprint))

(defn tipo-do-autorizador [pedido]
  (let [paciente (:paciente pedido)
        situacao (:situacao paciente)]
    (cond (= :urgente situacao) :sempre-autorizado
          (contains? paciente :plano) :plano-de-saude
          :else :credito-minimo)))


(defmulti deve-assinar-pre-autorizacao? tipo-do-autorizador)

(defmethod deve-assinar-pre-autorizacao? :sempre-autorizado [pedido] false)

(defmethod deve-assinar-pre-autorizacao? :plano-de-saude [pedido] (not (some #(= % (:procedimento pedido)) (:plano (:paciente pedido)))))

(defmethod deve-assinar-pre-autorizacao? :credito-minimo [pedido] (>= (:valor pedido 0) 50))


(let [particular {:id 15, :nome "Luana", :nascimento "15/02/1996", :situacao :normal}
      plano {:id 15, :nome "Luana", :nascimento "15/02/1996", :situacao :normal, :plano [:raio-x, :ultrasom]}]
  (pprint (deve-assinar-pre-autorizacao? {:paciente particular, :valor 1000, :procedimento :raio-x}))
  (pprint (deve-assinar-pre-autorizacao? {:paciente plano, :valor 1000, :procedimento :coleta-de-sangue})))