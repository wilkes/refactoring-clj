(ns video-store.movie)

(def ^:const REGULAR 0)
(def ^:const NEW-RELEASE 1)
(def ^:const CHILDRENS 2)

(defrecord Movie [title price-code])

(defn amount [movie days-rented]
  (condp = (-> movie :price-code)
    REGULAR (+ 2.0 (if (> days-rented 2)
                     (* 1.5 (- days-rented 2))
                     0))
    NEW-RELEASE (* 3.0 days-rented)
    CHILDRENS (+ 1.5 (if (> days-rented 3)
                       (* 1.5 (- days-rented 3))
                       0))))