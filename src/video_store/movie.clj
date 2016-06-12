(ns video-store.movie
  (:require [clojure.spec :as s]
            [clojure.spec.test :as test]))

(def ^:const REGULAR 0)
(def ^:const NEW-RELEASE 1)
(def ^:const CHILDRENS 2)

(s/def ::price-code #{REGULAR NEW-RELEASE CHILDRENS})
(s/def ::title (s/and string? (complement empty?)))
(s/def ::movie (s/keys :req [::title ::price-code]))

(defn make-movie [title price-code]
  {::title title ::price-code price-code})

(s/fdef make-movie
        :args (s/cat :title ::title :price-code ::price-code)
        :ret ::movie)

(defmulti amount (fn [movie _] (::price-code movie)))

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

(s/fdef amount
        :args (s/cat :movie ::movie :days (s/and integer? #(> % 0)))
        :ret (s/and double? #(>= % 1.5)))

(defmulti frequent-renter-points (fn [movie _] (::price-code movie)))

(defmethod frequent-renter-points NEW-RELEASE
  [_movie days-rented]
  (if (> days-rented 1) 2 1))

(defmethod frequent-renter-points :default
  [_movie _days-rented]
  1)

(s/fdef frequent-renter-points
        :args (s/cat :movie ::movie :days (s/and integer? #(> % 0)))
        :ret #{1 2})