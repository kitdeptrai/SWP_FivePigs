<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="vi">

    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>FIVEPIGS - Corrected Layout</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">

        <!-- font Noto Sans -->
        <link rel="preconnect" href="https://fonts.googleapis.com">
        <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
        <link href="https://fonts.googleapis.com/css2?family=Noto+Sans:ital,wght@0,100..900;1,100..900&display=swap"
              rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/customer.css">

            </head>

            <body>

                <jsp:include page="/WEB-INF/views/customer/sidebar.jsp">
                    <jsp:param name="activePage" value="home" />
                </jsp:include>

                <div class="main-content">
                    <jsp:include page="/WEB-INF/views/customer/header.jsp"></jsp:include>

            <!-- Homepage -->
            <div id="home" class="content-section active-section">
                <div class="fixed-layout-grid">
                    <div class="left-column">
                        <div class="featured-banner"
                             style="background-image: linear-gradient(to right, rgba(0,0,0,0.7), transparent), url('https://images.unsplash.com/photo-1513364776144-60967b0f800f?q=80&w=2071&auto=format&fit=crop');">
                            <h1 style="font-size: 32px; margin-bottom: 10px; line-height: 1.2;">Draw freely anytime,<br>with
                                ibisPaint</h1>
                            <p style="opacity: 0.9; margin-bottom: 20px;">Highly functional drawing app.</p>
                            <button
                                style="padding: 10px 25px; border: none; background: white; color: var(--primary-color);
                                font-weight: 700; border-radius: 8px; cursor: pointer; width: fit-content;">Install</button>
                        </div>
                        <div class="sub-banners-row">
                            <div class="sub-card">
                                <div class="sub-card-content">
                                    <h3>RAID</h3>
                                    <p style="font-size: 12px;">RPG • Free</p>
                                </div>
                            </div>
                            <div class="sub-card"
                                 style="background-image: url('https://play-lh.googleusercontent.com/y1vZtN3Vz7Q_T5XqjXqXqXqXqXqXqXqXqXqXqXqXqXqXqXqXqXqXqXqXqXqXqX'); background-color:#fce4ec;">
                                <div class="sub-card-content"
                                     style="background: linear-gradient(transparent, rgba(194, 24, 91, 0.8));">
                                    <h3>Cooking</h3>
                                    <p style="font-size: 12px;">Sim</p>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="right-column-box">
                        <div class="toggle-container">
                            <div id="home-trend" class="toggle-btn active" onclick="toggleList('home', 'trend')">TRENDING
                            </div>
                            <div id="home-best" class="toggle-btn" onclick="toggleList('home', 'best')">BEST SELLING</div>
                        </div>

                        <div class="scrollable-list" id="home-list-trend">

                            <div class="trend-item">
                                <img src="https://image.api.playstation.com/vulcan/img/rnd/202010/2119/cedX5oxDvA95beXB9mV9K1K6.png"
                                     class="trend-icon">
                                <div class="trend-info">
                                    <h4>Minecraft</h4>
                                    <div class="trend-sub">
                                        4.0 <i class="fa-solid fa-star"></i> | Social
                                    </div>
                                </div>
                                <div class="trend-action">Free</div>
                            </div>

                            <div class="trend-item">
                                <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/8/84/Spotify_icon.svg/1920px-Spotify_icon.svg.png"
                                     class="trend-icon">
                                <div class="trend-info">
                                    <h4>Spotify</h4>
                                    <div class="trend-sub">
                                        4.5 <i class="fa-solid fa-star"></i> | Music
                                    </div>
                                </div>
                                <div class="trend-action" style="color: #6b70ff;">Get</div>
                            </div>

                            <div class="trend-item">
                                <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/8/84/Spotify_icon.svg/1920px-Spotify_icon.svg.png"
                                     class="trend-icon">
                                <div class="trend-info">
                                    <h4>Spotify</h4>
                                    <div class="trend-sub">
                                        4.5 <i class="fa-solid fa-star"></i> | Music
                                    </div>
                                </div>
                                <div class="trend-action" style="color: #6b70ff;">Get</div>
                            </div>

                            <div class="trend-item">
                                <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/8/84/Spotify_icon.svg/1920px-Spotify_icon.svg.png"
                                     class="trend-icon">
                                <div class="trend-info">
                                    <h4>Spotify</h4>
                                    <div class="trend-sub">
                                        4.5 <i class="fa-solid fa-star"></i> | Music
                                    </div>
                                </div>
                                <div class="trend-action" style="color: #6b70ff;">Get</div>
                            </div>

                            <div class="trend-item">
                                <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/8/84/Spotify_icon.svg/1920px-Spotify_icon.svg.png"
                                     class="trend-icon">
                                <div class="trend-info">
                                    <h4>Spotify</h4>
                                    <div class="trend-sub">
                                        4.5 <i class="fa-solid fa-star"></i> | Music
                                    </div>
                                </div>
                                <div class="trend-action" style="color: #6b70ff;">Get</div>
                            </div>

                            <div class="trend-item">
                                <img src="https://upload.wikimedia.org/wikipedia/commons/thumb/8/84/Spotify_icon.svg/1920px-Spotify_icon.svg.png"
                                     class="trend-icon">
                                <div class="trend-info">
                                    <h4>Spotify</h4>
                                    <div class="trend-sub">
                                        4.5 <i class="fa-solid fa-star"></i> | Music
                                    </div>
                                </div>
                                <div class="trend-action" style="color: #6b70ff;">Get</div>
                            </div>



                            <div class="trend-item">
                                <img src="https://sf-tb-sg.ibytedtos.com/obj/eden-sg/uhtyvueh7nulogpoguhm/tiktok-icon2.png"
                                     class="trend-icon">
                                <div class="trend-info">
                                    <h4>TikTok</h4>
                                    <div class="trend-sub">
                                        4.2 <i class="fa-solid fa-star"></i> | Social
                                    </div>
                                </div>
                                <div class="trend-action" style="color: #6b70ff;">Open</div>
                            </div>

                            <div class="trend-item">
                                <img src="https://upload.wikimedia.org/wikipedia/commons/c/c7/Roblox_Logo_2022.jpg"
                                     class="trend-icon">
                                <div class="trend-info">
                                    <h4>Roblox</h4>
                                    <div class="trend-sub">
                                        4.8 <i class="fa-solid fa-star"></i> | Game
                                    </div>
                                </div>
                                <div class="trend-action" style="color: #6b70ff;">Get</div>
                            </div>

                        </div>

                        <div class="scrollable-list" id="home-list-best" style="display:none;">
                            <div class="trend-item">
                                <img src="https://wallpapers.com/images/hd/call-of-duty-black-ops-cold-war-key-art-4k-gaming-wallpaper-7c64b6w3a2a6z8y5.jpg"
                                     class="trend-icon">
                                <div class="trend-info">
                                    <h4>CoD: BO7</h4>
                                    <div class="trend-sub">5.0 <i class="fa-solid fa-star"></i> | FPS</div>
                                </div>
                                <div class="trend-action">$70</div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Game  -->
                <div class="section-header">
                    Open World Exploration Games <i class="fa-solid fa-chevron-right"></i>
                </div>

                <div class="app-list-grid">
                    <div class="app-list-item">
                        <img src="img/minecraft.webp" class="app-icon-lg">
                        <div class="app-details">
                            <div class="app-name">Minecraft</div>
                            <div class="app-meta"><i class="fa-solid fa-star"></i> 4.0 | Social</div>
                        </div>
                        <div class="app-price">Free</div>
                    </div>
                    <div class="app-list-item">
                        <img src="img/minecraft.webp" class="app-icon-lg">
                        <div class="app-details">
                            <div class="app-name">Minecraft</div>
                            <div class="app-meta"><i class="fa-solid fa-star"></i> 4.0 | Social</div>
                        </div>
                        <div class="app-price">Free</div>
                    </div>
                    <div class="app-list-item">
                        <img src="img/minecraft.webp" class="app-icon-lg">
                        <div class="app-details">
                            <div class="app-name">Minecraft</div>
                            <div class="app-meta"><i class="fa-solid fa-star"></i> 4.0 | Social</div>
                        </div>
                        <div class="app-price">Free</div>
                    </div>
                    <div class="app-list-item">
                        <img src="img/minecraft.webp" class="app-icon-lg">
                        <div class="app-details">
                            <div class="app-name">Minecraft</div>
                            <div class="app-meta"><i class="fa-solid fa-star"></i> 4.0 | Social</div>
                        </div>
                        <div class="app-price">Free</div>
                    </div>
                    <div class="app-list-item">
                        <img src="img/minecraft.webp" class="app-icon-lg">
                        <div class="app-details">
                            <div class="app-name">Minecraft</div>
                            <div class="app-meta"><i class="fa-solid fa-star"></i> 4.0 | Social</div>
                        </div>
                        <div class="app-price">Free</div>
                    </div>
                    <div class="app-list-item">
                        <img src="img/minecraft.webp" class="app-icon-lg">
                        <div class="app-details">
                            <div class="app-name">Minecraft</div>
                            <div class="app-meta"><i class="fa-solid fa-star"></i> 4.0 | Social</div>
                        </div>
                        <div class="app-price">Free</div>
                    </div>
                </div>

                <!-- Apps -->
                <div class="section-header">Social Networking Apps <i class="fa-solid fa-chevron-right"></i></div>
                <div class="app-list-grid">
                    <div class="app-list-item">
                        <img src="https://cdn-icons-png.flaticon.com/512/174/174855.png" class="app-icon-lg">
                        <div class="app-details">
                            <div class="app-name">Instagram</div>
                            <div class="app-meta"><i class="fa-solid fa-star"></i> 4.5 | Social</div>
                        </div>
                        <div class="app-price">Free</div>
                    </div>
                    <div class="app-list-item">
                        <img src="https://cdn-icons-png.flaticon.com/512/5968/5968764.png" class="app-icon-lg">
                        <div class="app-details">
                            <div class="app-name">Facebook</div>
                            <div class="app-meta"><i class="fa-solid fa-star"></i> 4.1 | Social</div>
                        </div>
                        <div class="app-price">Free</div>
                    </div>
                    <div class="app-list-item">
                        <img src="https://cdn-icons-png.flaticon.com/512/2111/2111463.png" class="app-icon-lg">
                        <div class="app-details">
                            <div class="app-name">TikTok</div>
                            <div class="app-meta"><i class="fa-solid fa-star"></i> 4.6 | Video</div>
                        </div>
                        <div class="app-price">Free</div>
                    </div>
                    <div class="app-list-item">
                        <img src="https://cdn-icons-png.flaticon.com/512/3670/3670051.png" class="app-icon-lg">
                        <div class="app-details">
                            <div class="app-name">WhatsApp</div>
                            <div class="app-meta"><i class="fa-solid fa-star"></i> 4.8 | Chat</div>
                        </div>
                        <div class="app-price">Free</div>
                    </div>
                    <div class="app-list-item">
                        <img src="https://cdn-icons-png.flaticon.com/512/3670/3670151.png" class="app-icon-lg">
                        <div class="app-details">
                            <div class="app-name">Twitter</div>
                            <div class="app-meta"><i class="fa-solid fa-star"></i> 4.0 | News</div>
                        </div>
                        <div class="app-price">Free</div>
                    </div>
                    <div class="app-list-item">
                        <img src="https://cdn-icons-png.flaticon.com/512/3670/3670147.png" class="app-icon-lg">
                        <div class="app-details">
                            <div class="app-name">YouTube</div>
                            <div class="app-meta"><i class="fa-solid fa-star"></i> 4.9 | Video</div>
                        </div>
                        <div class="app-price">Free</div>
                    </div>
                </div>
            </div>

        </div>

        <script src="${pageContext.request.contextPath}/assets/js/script.js"></script>
    </body>

</html>
