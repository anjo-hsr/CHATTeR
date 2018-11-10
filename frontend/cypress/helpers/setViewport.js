export default function setViewport(deviceName) {
  const deviceViewportMap = new Map([
    ['fullHD+', [2560, 1440]],
    ['fullHD', [1920, 1080]],
    ['HD+', [1366, 768]],
    ['ipad', [768, 1024]],
    ['ipad-pro', [834, 1112]],
    ['iphone-X', [375, 812]],
    ['iphone-6/7/8+', [414, 736]],
    ['iphone-6/7/8', [375, 667]],
    ['iphone-5', [320, 568]],
    ['iphone-3/4', [320, 480]],
    ['google-pixel', [411, 731]],
    ['samsung-galaxy-s8/9', [360, 740]],
    ['samsung-note-8', [414, 846]]
  ]);

  const viewport = deviceViewportMap.has(deviceName) ? deviceViewportMap.get(deviceName) : [1920, 1080];
  cy.viewport(viewport[0], viewport[1]);
}
