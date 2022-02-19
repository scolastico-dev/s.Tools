export default (context, inject) => {
  const animation = (
    element,
    animation,
    delay = 0,
    duration = 0.0,
    prefix = 'animate__'
  ) =>
    new Promise((resolve, reject) => {
      const animationName = `${prefix}${animation}`
      if (delay !== 0) element.classList.add(`animate__delay-${delay}s`)
      element.classList.add(`${prefix}animated`, animationName)
      if (duration !== 0.0)
        element.style.setProperty(
          '--animate-duration',
          duration.toString() + 's'
        )
      function handleAnimationEnd(event) {
        event.stopPropagation()
        element.classList.remove(`${prefix}animated`, animationName)
        if (delay !== 0) element.classList.remove(`animate__delay-${delay}s`)
        if (duration !== 0.0)
          element.style.setProperty('--animate-duration', null)
        resolve('Animation ended')
      }

      element.addEventListener('animationend', handleAnimationEnd, {
        once: true,
      })
    })
  inject('animation', animation)
}
