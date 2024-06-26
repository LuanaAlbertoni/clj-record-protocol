(ns hospital.aula2
  (:use clojure.pprint))


;(defrecord Paciente [id, nome, nascimento])

; Paciente Plano de Saude ====> paciente + plano de saude
; Paciente Particular ====> paciente


;Digitacao nao seria o maior problema
(defrecord PacienteParticular [id, nome, nascimento])
(defrecord PacientePlanoDeSaude [id, nome, nascimento, plano])


;Regras diferentes para tipos diferentes
;deve-assinar-pre-autorizacao?
; Particular ===> valor >= 50
;PlanodeSaude ===> procedimento nao esta no plano



(defprotocol Cobravel
  (deve-assinar-pre-autorizacao? [paciente procedimento valor]))

(extend-type PacienteParticular
  Cobravel
  (deve-assinar-pre-autorizacao? [paciente, procedimento, valor]
    (>= valor 50)))

(extend-type PacientePlanoDeSaude
  Cobravel
  (deve-assinar-pre-autorizacao? [paciente, procedimento, valor]
    (let [plano (:plano paciente)]
      (not (some #(= % procedimento) plano)))))

;contains? : verifica o indice... fica dependente da estrutura de dados

(let [particular (->PacienteParticular 15 "Luana" "15/02/1996")
      plano (->PacientePlanoDeSaude 15 "Luana" "15/02/1996", [:raio-x, :ultrasom])]
  (pprint (deve-assinar-pre-autorizacao? particular, :raio-x, 500))
  (pprint (deve-assinar-pre-autorizacao? plano, :raio-x, 500))
  )



(defprotocol Dateable
  (to-ms [this]))

(extend-type java.lang.Number
  Dateable
  (to-ms [this] this))

(pprint (to-ms 56))

(extend-type java.util.Date
  Dateable
  (to-ms [this] (.getTime this)))

(pprint (to-ms (java.util.Date.)))


(extend-type java.util.Calendar
  Dateable
  (to-ms [this] (to-ms (.getTime this))))

(pprint (to-ms (java.util.GregorianCalendar.)))











