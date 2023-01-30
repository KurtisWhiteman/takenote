import { getToken } from '@/helpers/auth/StorageHelpers'

export type DecodedToken = {
  id: string;
  sub: string;
  name: string;
  email: string;
  exp: number;
  iss: string;
}

/**
 * Decode the token : base64 decode and splitting
 */
export function decodeToken () {
  const token = getToken()
  if (token) {
    try {
      return JSON.parse(atob(token.split('.')[1]))
      // eslint-disable-next-line no-empty
    } catch (e) {
    }
  }
  return null
}

/**
 * Determine if token is not expired
 */
export function isTokenValid (): boolean {
  const parsedToken = decodeToken()
  return parsedToken && Date.now() < parsedToken.exp * 1000
}
