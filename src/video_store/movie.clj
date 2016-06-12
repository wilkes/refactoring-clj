(ns video-store.movie)

(def ^:const REGULAR 0)
(def ^:const NEW-RELEASE 1)
(def ^:const CHILDRENS 2)

(defrecord Movie [title price-code])