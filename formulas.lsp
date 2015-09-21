(defvar *formulas*
  '((= s (* 0.5 (* a (expt t 2))))
    (= s (+ s0 (* v t)))
    (= a (/ f m))
    (= v (* a t))
    (= f (/ (* m v) t))
    (= f (/ (* m (expt v 2)) r))
    (= h (- h0 (* 4.94 (expt t 2))))
    (= c (sqrt (+ (expt a 2) (expt b 2))))
    (= v (* v0 (- 1.0 (exp (/ (- t) (* r c))))))
    ))

(defvar *opposites*
  '((+ -) (- +) (* /) (/ *) (sqrt expt) (expt sqrt) (log exp) (exp log) ) )
