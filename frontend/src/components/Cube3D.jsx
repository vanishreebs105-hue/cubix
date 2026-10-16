import { Canvas } from '@react-three/fiber';
import { OrbitControls, RoundedBox } from '@react-three/drei';

/**
 * A single cubie (small cube that makes up the big cube).
 */
function Cubie({ position, colors }) {
  // colors: [right, left, top, bottom, front, back]
  return (
    <group position={position}>
      {/* Base cube — the plastic body */}
      <RoundedBox args={[0.98, 0.98, 0.98]} radius={0.06} smoothness={4}>
        <meshStandardMaterial color="#0f0f0f" roughness={0.6} metalness={0.1} />
      </RoundedBox>

      {/* Colored stickers on each face (only visible ones) */}
      {colors[0] && <Sticker position={[0.5, 0, 0]} rotation={[0, Math.PI / 2, 0]} color={colors[0]} />}
      {colors[1] && <Sticker position={[-0.5, 0, 0]} rotation={[0, -Math.PI / 2, 0]} color={colors[1]} />}
      {colors[2] && <Sticker position={[0, 0.5, 0]} rotation={[-Math.PI / 2, 0, 0]} color={colors[2]} />}
      {colors[3] && <Sticker position={[0, -0.5, 0]} rotation={[Math.PI / 2, 0, 0]} color={colors[3]} />}
      {colors[4] && <Sticker position={[0, 0, 0.5]} rotation={[0, 0, 0]} color={colors[4]} />}
      {colors[5] && <Sticker position={[0, 0, -0.5]} rotation={[0, Math.PI, 0]} color={colors[5]} />}
    </group>
  );
}

/**
 * A single colored sticker face.
 */
function Sticker({ position, rotation, color }) {
  return (
    <mesh position={position} rotation={rotation}>
      <planeGeometry args={[0.85, 0.85]} />
      <meshStandardMaterial
        color={color}
        roughness={0.3}
        metalness={0.05}
        emissive={color}
        emissiveIntensity={0.08}
      />
    </mesh>
  );
}

/**
 * Builds the full 3x3x3 cube using standard Rubik's colors.
 */
function RubiksCube() {
  const COLORS = {
    U: '#FFFFFF', // White — Up
    D: '#FFD500', // Yellow — Down
    R: '#C41E3A', // Red — Right
    L: '#FF8C00', // Orange — Left
    F: '#009E60', // Green — Front
    B: '#0051BA', // Blue — Back
    INNER: null,
  };

  const cubies = [];

  for (let x = -1; x <= 1; x++) {
    for (let y = -1; y <= 1; y++) {
      for (let z = -1; z <= 1; z++) {
        // Only place visible cubies (skip the center one)
        if (x === 0 && y === 0 && z === 0) continue;

        const colors = [
          x === 1 ? COLORS.R : COLORS.INNER, // Right
          x === -1 ? COLORS.L : COLORS.INNER, // Left
          y === 1 ? COLORS.U : COLORS.INNER, // Up
          y === -1 ? COLORS.D : COLORS.INNER, // Down
          z === 1 ? COLORS.F : COLORS.INNER, // Front
          z === -1 ? COLORS.B : COLORS.INNER, // Back
        ];

        cubies.push(
          <Cubie key={`${x}-${y}-${z}`} position={[x, y, z]} colors={colors} />
        );
      }
    }
  }

  return <group>{cubies}</group>;
}

/**
 * The 3D Canvas containing the cube.
 */
export default function Cube3D() {
  return (
    <div className="w-full h-full">
      <Canvas
        camera={{ position: [5, 5, 5], fov: 45 }}
        dpr={[1, 2]}
        gl={{ antialias: true }}
      >
        {/* Lighting */}
        <ambientLight intensity={0.65} />
        <directionalLight position={[5, 8, 5]} intensity={1.1} castShadow />
        <directionalLight position={[-5, -3, -5]} intensity={0.4} />
        <pointLight position={[0, 5, 0]} intensity={0.5} />

        {/* The cube */}
        <RubiksCube />

        {/* Interactive controls — drag to rotate */}
        <OrbitControls
          enablePan={false}
          enableZoom={true}
          minDistance={6}
          maxDistance={14}
          autoRotate={true}
          autoRotateSpeed={0.8}
        />
      </Canvas>
    </div>
  );
}