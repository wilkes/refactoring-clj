(ns video-store.movie)

(def ^:const REGULAR 0)
(def ^:const NEW-RELEASE 1)
(def ^:const CHILDRENS 2)

(defrecord Movie [title price-code])

(defmulti amount (fn [movie _] (:price-code movie)))

(defmethod amount REGULAR
  [_movie days-rented]
  (+ 2.0 (if (> days-rented 2)
           (* 1.5 (- days-rented 2))
           0)))

(defmethod amount NEW-RELEASE
  [_movie days-rented]
  (* 3.0 days-rented))

(defmethod amount CHILDRENS
  [_movie days-rented]
  (+ 1.5 (if (> days-rented 3)
           (* 1.5 (- days-rented 3))
           0)))

(defmulti frequent-renter-points (fn [movie _] (:price-code movie)))

(defmethod frequent-renter-points NEW-RELEASE
  [_movie days-rented]
  (if (> days-rented 1) 2 1))

(defmethod frequent-renter-points :default
  [_movie days-rented]
  1)