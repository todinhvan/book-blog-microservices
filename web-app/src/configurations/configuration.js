export const CONFIG = {
  API_GATEWAY: "http://localhost:9000/api/v1",
};

export const API = {
  LOGIN: "/identity/auth/login",
  MY_INFO: "/profiles/user",
  MY_POST: "/posts",
  UPDATE_PROFILE: "/profiles/user/update",
  UPDATE_AVATAR: "/profiles/user/change-avatar",
};

export const OAuthConfig = {
  clientId: "333056284830-9vbovj6v2ji9c7k430a1umn9utcj74ra.apps.googleusercontent.com",
  redirectUri: "http://localhost:3000/authenticate",
  authUri: "https://accounts.google.com/o/oauth2/auth",
};